from ..java import opcodes as JavaOpcodes, Classref


##########################################################################
# Pseudo instructions used to flag the offset position of other
# attributes of the code, especially those depened on opcode offset.
##########################################################################

# A sentinel value to mark jumps that need to be resolved.
RESOLVE = object()


class IF:
    """Mark the start of an if-endif block.

    This forms the first of at least two pseudo-operations. It might be
    as simple as:
        IF([], IFOPCODE)
            ...
        END_IF()

    or as complex as

        IF([
                ... # commands to prepare stack for IF opcode
            ],
            IFOPCODE
        )
            ...
        ELIF([
                ... # commands to prepare stack for next IF opcode
            ],
            IFOPCODE
        )
            ...
        ELIF([
                ... # commands to prepare stack for next IF opcode
            ],
            IFOPCODE
        )
            ...
        ELSE()
            ...
        END_IF()

    END_IF comes *after* the last operation in the relevant block;
    but IF, ELIF and ELSE come *before* the first instruction. A little
    special handling is required to account for this.

        1. When the IF is created, it is empty, and has no start_op.
           The empty pseudo-instruction is added to the if_blocks list.
        2. When the instruction *after* the IF, ELIF or ELSE is processed,
           it is post-processed to fill in the operation marking the
           start of the relevant section.
        3. When the first ELIF/ELSE is found, that marks the end of the IF; a
           new elif block is registered. A GOTO is added, with no offset,
           to the end of the IF block. The offset will be updated
           when the END_IF is found.
        3. When the 2+ ELIF/ELSE is found, that marks the end of the previous
           ELIF; a new elif block is registered. A GOTO is added, with no offset,
           to the end of the previous block. The offset will be updated
           when the END_IF is found.
        4. When an END_IF is found, that marks the end of the current
           IF-ELIF-ELSE block. An end_op is recorded; that means this if block
           is no longer current.

    IF-ELSE blocks can be nested, so when an ELIF, ELSE or END_IF is found,
    it is recorded against the last IF in the if_blocks list
    that has no end_op recorded.

    """
    def __init__(self, commands, opcode):
        # The commands to prepare the stack for the IF comparison
        self.commands = commands if commands is not None else []
        # print("CREATE IF", id(self), opcode)

        # The opcode class to instantiate
        self.opcode = opcode

        # The opcode that is the first in the formal IF block
        self.start_op = None

        # The instantiated opcode for the comparison
        self.if_op = None

        # The list of all ELSE blocks associated with this IF
        self.elifs = []

        # The jump operation at the end of the IF, jumping to the
        # end of the IF-ELSE block.
        self.jump_op = None

        # The last operation in the IF-ELSE block - the target for
        # all if-exiting jumps.
        self.end_op = None

        # If this IF can be identified as the start of a new
        # line of source code, track that line.
        self.starts_line = None

    def process(self, context):
        # Add the stack prepration commands to the code list
        # print("PROCESS IF", id(self))
        context.add_opcodes(*self.commands)

        # Create an instance of the opcode and put it on the code list
        self.if_op = self.opcode(0)
        context.add_opcodes(self.if_op)

        # If there were commands, the first command is the start
        # of the IF block. oetherwise, it's the if_op.
        try:
            self.start_op = self.commands[0]
        except IndexError:
            self.start_op = self.if_op

        # Record this IF.
        context.if_blocks.append(self)
        # print("IF BLOCKS: ", [(id(b), b.end_op) for b in context.if_blocks])

        # This opcode isn't for the final output.
        return False


class ELIF:
    def __init__(self, commands, opcode):
        # The master IF block
        self.if_block = None
        # print("CREATE %s" % "ELIF" if opcode else "ELSE", id(self))

        # The commands to prepare the stack for the IF comparison
        self.commands = commands if commands is not None else []

        # The opcode class to instantiate
        self.opcode = opcode

        # The instantiated opcode for the comparison
        self.elif_op = None

        # The jump operation at the end of the ELIF, jumping to the
        # end of the IF-ELSE block.
        self.jump_op = None

        # If this ELIF can be identified as the start of a new
        # line of source code, track that line.
        self.starts_line = None

    def process(self, context):
        # Find the most recent if block on the stack that hasn't been
        # ended. That's the block that the elif applies to.
        # print("PROCESS ELIF(or else)")
        for if_block in context.if_blocks[::-1]:
            if if_block.end_op is None:
                if_block.end_op = RESOLVE
                break
        # print("    if block is", id(if_block))

        # If this is the first elif, add a GOTO and use it as the
        # jump operation at the end of the IF block. If there are
        # ELIFs, add the GOTO as the jump operation on the most
        # recent ELIF.
        jump_op = JavaOpcodes.GOTO(0)
        context.add_opcodes(jump_op)

        if len(if_block.elifs) == 0:
            # print("    first elif")
            if_block.jump_op = jump_op
        else:
            # print("    already got an endif")
            if_block.handlers[-1].jump_op = jump_op

        if self.opcode:
            # print("    this is an elif")
            # Add the stack prepration commands to the code list
            context.add_opcodes(*self.commands)

            # Create an instance of the opcode and put it on the code list
            self.if_op = self.opcode(0)
            context.add_opcodes(self.if_op)
        # else:
            # print("    this is an else")

        if_block.elifs.append(self)
        # print("IF BLOCKS: ", [(id(b), b.end_op) for b in context.if_blocks])

        # This opcode isn't for the final output.
        return False


class ELSE(ELIF):
    def __init__(self):
        # ELSE if an ELIF with no preparation and no comparison opcode
        super().__init__(None, None)
        # print("CREATE ELSE", id(self))


class END_IF:
    def __init__(self):
        # print("CREATE ENDIF")

        # If this IF can be identified as the start of a new
        # line of source code, track that line.
        self.starts_line = None

    def process(self, context):
        # Find the most recent if block on the stack that hasn't been
        # ended. That's the block that the elif applies to.
        # print("PROCESS ENDIF")
        for if_block in context.if_blocks[::-1]:
            if if_block.end_op is None:
                if_block.end_op = RESOLVE
                break
        # print("    if block is", id(if_block))

        # The next opcode is the end of the IF-ELIF-ELSE block.
        context.next_resolve_list.append((if_block, 'end_op'))
        # print("IF BLOCKS: ", [(id(b), b.end_op) for b in context.if_blocks])

        # This opcode isn't for the final output.
        return False


class TRY:
    """Mark the start of a try-catch block.

    This forms the first of at least three pseudo-operations:
        TRY()
            ...
        CATCH('your/exception/descriptor')
            ...
        CATCH('your/other/exception')
            ...
        END_TRY()

    END_TRY come *after* the last operation in the relevant block;
    but TRY and CATCH come *before* the first instruction. A little
    special handling is required to account for this.

        1. When the TRY is created, it is empty, and has no start_op.
           The empty pseudo-instruction is added to the exception list.
        2. When the instruction *after* the TRY or CATCH is processed,
           it is post-processed to fill in the operation marking the
           start of the relevant section.
        3. When a CATCH is found, that marks the end of the TRY; a
           new handler is registered in the current exception.
           If it's the first CATCH, a GOTO is added, with no offset,
           and recorded as the jump_op. The offset will be updated
           when the END is found.
        4. When an END is found, that marks the end of the current
           exception. An end_op is recorded; that means this try block
           is no longer current.

    Exception blocks can be nested, so when a CATCH or END is found,
    it is recorded against the last exception in the exception list
    that has no end_op recorded.

    """
    def __init__(self):
        self.start_op = None
        self.jump_op = None
        self.end_op = None
        self.handlers = []
        # print("CREATE TRY", id(self))

        # If this TRY can be identified as the start of a new
        # line of source code, track that line.
        self.starts_line = None

    def process(self, context):
        # print("PROCESS TRY", id(self))
        context.try_catches.append(self)
        # print("    try-catches", [(id(t), t.end_op) for t in context.try_catches])

        # The next opcode is the start of the TRY block.
        context.next_resolve_list.append((self, 'start_op'))

        # This opcode isn't for the final output.
        return False


class CATCH:
    def __init__(self, descriptor):
        self.descriptor = descriptor
        # print("CREATE CATCH", id(self), self.descriptor)

        # If this CATCH can be identified as the start of a new
        # line of source code, track that line.
        self.starts_line = None

        self.jump_op = None

    def __len__(self):
        # The CATCH needs to be able to pass as an opcode under initial
        # post processing
        return 3

    def process(self, context):
        # Find the most recent exception on the stack that hasn't been
        # ended. That's the block that the catch applies to.
        # print("PROCESS CATCH", id(self))
        for exception in context.try_catches[::-1]:
            if exception.end_op is None:
                break

        # print("    current exception", exception)
        # print("    try-catches", [(id(t), t.end_op) for t in context.try_catches])

        # If this is the first catch, insert a GOTO operation.
        # The jump distance will be updated when all the CATCH blocks
        # have been processed and the exception is converted.
        # If it isn't the first catch, then this catch concludes the
        # previous one. Add a goto to the end of the block, and
        # record the end of the block for framing purposes.
        if len(exception.handlers) == 0:
            # print("    first catch")
            exception.jump_op = JavaOpcodes.GOTO(0)
            context.add_opcodes(exception.jump_op)
        else:
            # print("    nth catch")
            end_jump = JavaOpcodes.GOTO(0)
            exception.handlers[-1].jump_op = end_jump
            context.add_opcodes(end_jump)
            exception.handlers[-1].end_op = context.code[-1]

        # Add this catch block as a handler
        exception.handlers.append(self)

        # The next opcode is the start of the catch block.
        context.next_resolve_list.append((self, 'start_op'))

        # This opcode isn't for the final output.
        return False


class END_TRY:
    def __init__(self):
        # print("CREATE END TRY", id(self))

        # If this END TRY can be identified as the start of a new
        # line of source code, track that line.
        self.starts_line = None

    def process(self, context):
        # Find the most recent exception on the stack that hasn't been
        # ended. That's the block we're ending.
        # print("PROCESS END TRY", id(self))
        for exception in context.try_catches[::-1]:
            if exception.end_op is None:
                exception.end_op = RESOLVE
                break

        # print("    current exception", exception)
        # print("    try-catches", [(id(t), t.end_op) for t in context.try_catches])

        exception.handlers[-1].end_op = context.code[-1]

        # The next opcode is the end of the try-catch block.
        context.next_resolve_list.append((exception, 'end_op'))

        # This opcode isn't for the final output.
        return False


##########################################################################
# Local variables are stored in a dictionary, keyed by name,
# and with the value of the local variable register they are stored in.
#
# When a variable is deleted, a value of None is put in as the
# value.
##########################################################################

# A marker for deleted variable names.
DELETED = object()


def ALOAD_name(context, name):
    """Generate the opcode to load a variable with the given name onto the stack.

    This looks up the local variable dictionary to find which
    register is being used for that variable, using the optimized
    register operations for the first 4 local variables.
    """
    i = context.localvars[name]
    if i == DELETED:
        raise KeyError(name)

    if i == 0:
        return JavaOpcodes.ALOAD_0()
    elif i == 1:
        return JavaOpcodes.ALOAD_1()
    elif i == 2:
        return JavaOpcodes.ALOAD_2()
    elif i == 3:
        return JavaOpcodes.ALOAD_3()
    else:
        return JavaOpcodes.ALOAD(i)


def ASTORE_name(context, name):
    """Generate the opcode to store a variable with the given name.

    This looks up the local variable dictionary to find which
    register is being used for that variable, using the optimized
    register operations for the first 4 local variables.
    """
    try:
        i = context.localvars[name]
        if i == DELETED:
            context.localvars[name] = i
    except KeyError:
        i = len(context.localvars)
        context.localvars[name] = i

    if i == 0:
        return JavaOpcodes.ASTORE_0()
    elif i == 1:
        return JavaOpcodes.ASTORE_1()
    elif i == 2:
        return JavaOpcodes.ASTORE_2()
    elif i == 3:
        return JavaOpcodes.ASTORE_3()
    else:
        return JavaOpcodes.ASTORE(i)


def ADELETE_name(context, name):
    """Remove a name from the localvar pool
    """
    try:
        value = context.localvars[name]
        if value == DELETED:
            raise KeyError("Local variable '%s' already deleted" % name)
        context.localvars[name] = DELETED
    except KeyError:
        raise


def ICONST_val(value):
    """Write a constant onto the stack.

    There are a couple of opcodes that can be used to optimize the
    loading of small integers; use them if possible.
    """
    if value == 0:
        return JavaOpcodes.ICONST_0()
    elif value == 1:
        return JavaOpcodes.ICONST_1()
    elif value == 2:
        return JavaOpcodes.ICONST_2()
    elif value == 3:
        return JavaOpcodes.ICONST_3()
    elif value == 4:
        return JavaOpcodes.ICONST_4()
    elif value == 5:
        return JavaOpcodes.ICONST_5()
    elif value == -1:
        return JavaOpcodes.ICONST_M1()
    else:
        return JavaOpcodes.LDC(value)


def resolve_jump(opcode, context, target, position):
    """Resolve a jump target in an opcode.

    target is the code offset in the Python code space.
    When Python code is converted to Java, it will turn into
    0-N opcodes. We need to specify which one will be used
    as the Java offset:
     * START - the first Java opcode generated from this Python opcode
     * END - the last Java opcode generated from the Python opcode
     * NEXT - the next Java opcode added after this block.
    """
    try:
        # print("RESOLVE %s %s to %s %s" % (opcode, id(opcode), target, position))
        py_opcode = context.jump_targets[target]
        # print("PY OPCODE:", py_opcode)
        if position == Opcode.START:
            opcode.jump_op = py_opcode.start_op
        elif position == Opcode.END:
            opcode.jump_op = py_opcode.end_op
        elif position == Opcode.NEXT:
            # If we are resolving a forward reference, the 'next'
            # opcode won't be known yet; so put it on the resolve list.
            try:
                opcode.jump_op = py_opcode.next_op
            except AttributeError:
                context.next_resolve_list.append((opcode, 'jump_op'))

        else:
            raise Exception("Unknown opcode position")

        context.jumps.append(opcode)
        try:
            opcode.jump_op.references.append(opcode)
        except AttributeError:
            pass

    except KeyError:
        # print("    unknown reference %s %s... defer" % (opcode, id(opcode)))
        context.unknown_jump_targets.setdefault(target, []).append((opcode, position))

    return opcode


##########################################################################
# Base classes for defining opcodes.
##########################################################################

class Opcode:
    START = 10
    END = 20
    NEXT = 30

    start_block = False
    end_block = False

    def __init__(self, code_offset, starts_line, is_jump_target):
        self.code_offset = code_offset
        self.starts_line = starts_line
        self.is_jump_target = is_jump_target

    @property
    def opname(self):
        return self.__class__.__name__

    def __repr__(self):
        return self.opname + ': ' + self.__arg_repr__()

    def __arg_repr__(self):
        return ''

    def transpile(self, context, arguments):
        # print("TRANSPILE %s:%4d %s" % ('%4d' % self.starts_line if self.starts_line else '    ', self.code_offset, self))

        # If the Python opcode marks the start of a line of code,
        # transfer that relationship to the first opcode in the
        # generated Java code.
        if self.starts_line:
            context.next_opcode_starts_line = self.starts_line

        # If this command is a jump target, then track
        # the startop in the command sequence.
        if self.is_jump_target:
            context.next_resolve_list.append((self, 'start_op'))
            n_ops = len(context.code)

        # Actually convert the opcode. This is recursive down the Command sequence.
        self.convert(context, arguments)

        # If this command is a jump target, then track
        # the end and next op in the command sequence.
        if self.is_jump_target:
            if len(context.code) == n_ops:
                context.next_resolve_list.append((self, 'end_op'))
            else:
                self.end_op = context.code[-1]

            context.next_resolve_list.append((self, 'next_op'))

            # Save the code offset for the jump operation.
            context.jump_targets[self.code_offset] = self

            # If this opcode has been a forward-referenced as a jump
            # target, go back and resolve the reference.
            try:
                references = context.unknown_jump_targets.pop(self.code_offset)
                # print("   resolving %s references to offset %s" % (len(references), self.code_offset))
                for opcode, position in references:
                    resolve_jump(opcode, context, self.code_offset, position)

            except KeyError:
                # print("   No unknown jump target at %s" % self.code_offset)
                pass


class UnaryOpcode(Opcode):
    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        context.add_opcodes(
            JavaOpcodes.INVOKEVIRTUAL(
                'org/python/Object',
                self.__method__,
                '()Lorg/python/Object;'
            )
        )


class BinaryOpcode(Opcode):
    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        # The first argument to an inplace operator is the command
        # that the operator is going to be invoked on.
        arguments[0].operation.transpile(context, arguments[0].arguments)

        context.add_opcodes(
            # Create the args array
            ICONST_val(len(arguments) - 1),
            JavaOpcodes.ANEWARRAY('org/python/Object'),
        )
        for i, argument in enumerate(arguments[1:]):
            context.add_opcodes(
                JavaOpcodes.DUP(),
                ICONST_val(i),
            )
            argument.operation.transpile(context, argument.arguments)
            context.add_opcodes(
                JavaOpcodes.AASTORE(),
            )

        context.add_opcodes(
            # Then add an (empty) kswargs dict.
            JavaOpcodes.NEW('java/util/Hashtable'),
            JavaOpcodes.DUP(),
            JavaOpcodes.INVOKESPECIAL('java/util/Hashtable', '<init>', '()V'),

            JavaOpcodes.INVOKEVIRTUAL(
                'org/python/Object',
                self.__method__,
                '([Lorg/python/Object;Ljava/util/Hashtable;)Lorg/python/Object;'
            )
        )


class InplaceOpcode(Opcode):
    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        # The first argument to an inplace operator is the command
        # that the operator is going to be invoked on.
        arguments[0].operation.transpile(context, arguments[0].arguments)

        context.add_opcodes(
            # Duplicate the first argument, because it will be both the
            # object on which the function is called, and the return value.
            JavaOpcodes.DUP(),

            # Create the args array
            ICONST_val(len(arguments) - 1),
            JavaOpcodes.ANEWARRAY('org/python/Object'),
        )
        for i, argument in enumerate(arguments[1:]):
            context.add_opcodes(
                JavaOpcodes.DUP(),
                ICONST_val(i),
            )
            argument.operation.transpile(context, argument.arguments)
            context.add_opcodes(
                JavaOpcodes.AASTORE(),
            )

        context.add_opcodes(
            # Then add an (empty) kswargs dict.
            JavaOpcodes.NEW('java/util/Hashtable'),
            JavaOpcodes.DUP(),
            JavaOpcodes.INVOKESPECIAL('java/util/Hashtable', '<init>', '()V'),

            JavaOpcodes.INVOKEVIRTUAL(
                'org/python/Object',
                self.__method__,
                '([Lorg/python/Object;Ljava/util/Hashtable;)V'
            )
        )


##########################################################################
# The actual Python opcodes
##########################################################################

class POP_TOP(Opcode):
    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        # Ignore the top of the stack.
        context.add_opcodes(JavaOpcodes.POP())


class ROT_TWO(Opcode):
    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 2

    def convert(self, context, arguments):
        context.add_opcodes(JavaOpcodes.SWAP())


# class ROT_THREE(Opcode):


class DUP_TOP(Opcode):
    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 2

    def convert(self, context, arguments):
        context.add_opcodes(JavaOpcodes.DUP())


class DUP_TOP_TWO(Opcode):
    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 4

    def convert(self, context, arguments):
        context.add_opcodes(JavaOpcodes.DUP2())


class NOP(Opcode):
    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        context.add_opcodes(JavaOpcodes.NOP())


class UNARY_POSITIVE(UnaryOpcode):
    __method__ = '__pos__'


class UNARY_NEGATIVE(UnaryOpcode):
    __method__ = '__neg__'


# FIXME - there shouldn't be a __not__...
class UNARY_NOT(UnaryOpcode):
    __method__ = '__not__'


class UNARY_INVERT(UnaryOpcode):
    __method__ = '__invert__'


class BINARY_POWER(BinaryOpcode):
    __method__ = '__pow__'


class BINARY_MULTIPLY(BinaryOpcode):
    __method__ = '__mul__'


class BINARY_MODULO(BinaryOpcode):
    __method__ = '__mod__'


class BINARY_ADD(BinaryOpcode):
    __method__ = '__add__'


class BINARY_SUBTRACT(BinaryOpcode):
    __method__ = '__sub__'


class BINARY_SUBSCR(BinaryOpcode):
    __method__ = '__subscr__'


class BINARY_FLOOR_DIVIDE(BinaryOpcode):
    __method__ = '__floordiv__'


class BINARY_TRUE_DIVIDE(BinaryOpcode):
    __method__ = '__truediv__'


class INPLACE_FLOOR_DIVIDE(InplaceOpcode):
    __method__ = '__ifloordiv__'


class INPLACE_TRUE_DIVIDE(InplaceOpcode):
    __method__ = '__itruediv__'


# class STORE_MAP(Opcode):

class INPLACE_ADD(InplaceOpcode):
    __method__ = '__iadd__'


class INPLACE_SUBTRACT(InplaceOpcode):
    __method__ = '__isub__'


class INPLACE_MULTIPLY(InplaceOpcode):
    __method__ = '__imul__'


class INPLACE_MODULO(InplaceOpcode):
    __method__ = '__imod__'


# class STORE_SUBSCR(Opcode):
# class DELETE_SUBSCR(Opcode):


class BINARY_LSHIFT(BinaryOpcode):
    __method__ = '__lshift__'


class BINARY_RSHIFT(BinaryOpcode):
    __method__ = '__rshift__'


class BINARY_AND(BinaryOpcode):
    __method__ = '__and__'


class BINARY_XOR(BinaryOpcode):
    __method__ = '__xor__'


class BINARY_OR(BinaryOpcode):
    __method__ = '__or__'


class INPLACE_POWER(InplaceOpcode):
    __method__ = '__ipow__'


class GET_ITER(Opcode):
    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        context.add_opcodes(
            ASTORE_name(context, '##ITERABLE##'),
            JavaOpcodes.ICONST_1(),
            JavaOpcodes.ANEWARRAY('org/python/Object'),
            JavaOpcodes.DUP(),
            JavaOpcodes.ICONST_0(),
            ALOAD_name(context, '##ITERABLE##'),
            JavaOpcodes.AASTORE(),

            JavaOpcodes.NEW('java/util/Hashtable'),
            JavaOpcodes.DUP(),
            JavaOpcodes.INVOKESPECIAL('java/util/Hashtable', '<init>', '()V'),

            JavaOpcodes.INVOKESTATIC(
                'org/Python',
                'iter',
                '([Lorg/python/Object;Ljava/util/Hashtable;)Lorg/python/Object;'
            ),

            JavaOpcodes.CHECKCAST('org/python/Iterator'),
        )


class PRINT_EXPR(Opcode):
    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 0


class LOAD_BUILD_CLASS(Opcode):
    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 1


# class YIELD_FROM(Opcode):

class INPLACE_LSHIFT(InplaceOpcode):
    __method__ = '__ilshift__'


class INPLACE_RSHIFT(InplaceOpcode):
    __method__ = '__irshift__'


class INPLACE_AND(InplaceOpcode):
    __method__ = '__iand__'


class INPLACE_XOR(InplaceOpcode):
    __method__ = '__ixor__'


class INPLACE_OR(InplaceOpcode):
    __method__ = '__ior__'


# class BREAK_LOOP(Opcode):
# class WITH_CLEANUP(Opcode):
#     end_block = True

class RETURN_VALUE(Opcode):
    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        context.add_opcodes(JavaOpcodes.ARETURN())


# class IMPORT_STAR(Opcode):
# class YIELD_VALUE(Opcode):


class POP_BLOCK(Opcode):
    end_block = True

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        # print("convert POP_BLOCK", len(arguments))
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)


class END_FINALLY(Opcode):
    end_block = 'finally'

    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        # print("convert END_FINALLY ", [arg.operation for arg in arguments])

        context.add_opcodes(TRY())
        for argument in arguments[0].arguments[1:]:
            argument.operation.transpile(context, argument.arguments)

        n_handlers = len(arguments) // 4
        for i in range(n_handlers, 0, -1):
            exc_name = arguments[-4 * i].arguments[0].arguments[0].arguments[0].arguments[0].arguments[-1].operation.name
            var_name = arguments[-4 * i].arguments[0].operation.name

            # Define the exception handler.
            # On entry to the exception, the stack will contain
            # a single value - the exception being thrown.
            # This exception must be wrapped into an org/python/Object
            # so it can be used as an argument elsewhere.
            context.add_opcodes(
                CATCH('org/python/exceptions/%s' % exc_name),
                ASTORE_name(context, var_name),
                JavaOpcodes.NEW('org/python/Object'),
                JavaOpcodes.DUP(),
                ALOAD_name(context, var_name),
                JavaOpcodes.INVOKESPECIAL('org/python/Object', '<init>', '(Lorg/python/exceptions/BaseException;)V'),
                ASTORE_name(context, var_name),
            )

            # Then roll out the body of the exception handler.
            for argument in arguments[-4 * i + 1].arguments[1:]:
                argument.operation.transpile(context, argument.arguments)

        context.add_opcodes(END_TRY())


class POP_EXCEPT(Opcode):
    start_block = 'finally'

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        # print("convert POP_EXCEPT", len(arguments))
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)


class STORE_NAME(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        # Depending on context, this might mean writing to local
        # variables, class attributes, or to the global context.
        context.store_name(self.name, arguments)


class DELETE_NAME(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        # Depending on context, this might mean deleting from local
        # variables, class attributes, or to the global context.
        context.delete_name(self.name, arguments)


class UNPACK_SEQUENCE(Opcode):
    def __init__(self, count, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.count = count

    def __arg_repr__(self):
        return str(self.count)

    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return self.count

    # def convert(self, context, arguments):
    #     for argument in arguments:
    #         argument.operation.transpile(context, argument.arguments)


class FOR_ITER(Opcode):
    start_block = 'for-loop'

    def __init__(self, target, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.target = target

    def __arg_repr__(self):
        return str(self.target)

    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 2

    def convert(self, context, arguments):
        context.add_opcodes(TRY())

        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        context.add_opcodes(
                JavaOpcodes.DUP(),
                JavaOpcodes.INVOKEINTERFACE('org/python/Iterator', '__next__', '()Lorg/python/Object;', 0),
            CATCH('org/python/exceptions/StopIteration'),
                resolve_jump(JavaOpcodes.GOTO(0), context, self.target, Opcode.NEXT),
            END_TRY()
        )


# class UNPACK_EX(Opcode):


class STORE_ATTR(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        # FIXME
        arguments[1].operation.transpile(context, arguments[1].arguments)
        context.add_opcodes(JavaOpcodes.LDC(self.name))
        arguments[0].operation.transpile(context, arguments[0].arguments)

        context.add_opcodes(
            JavaOpcodes.INVOKEVIRTUAL('org/python/Object', '__setattr__', '(Ljava/lang/String;Lorg/python/Object;)V'),
        )


# class DELETE_ATTR(Opcode):

class STORE_GLOBAL(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        # Depending on context, this might mean writing to local
        # variables, class attributes, or to the global context.
        context.store_name(self.name, arguments, allow_locals=False)


# class DELETE_GLOBAL(Opcode):


class LOAD_CONST(Opcode):
    def __init__(self, const, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.const = const

    def __arg_repr__(self):
        return str(self.const)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        # A None value has it's own opcode.
        # If the constant is a byte or a short, we can
        # cut a value out of the constant pool.
        # Otherwise, use LDC.
        if self.const is None:
            context.add_opcodes(JavaOpcodes.ACONST_NULL())
            return
        elif isinstance(self.const, int):
            prototype = '(I)V'
            if self.const == 0:
                load_op = JavaOpcodes.ICONST_0()
            elif self.const == 1:
                load_op = JavaOpcodes.ICONST_1()
            elif self.const == 2:
                load_op = JavaOpcodes.ICONST_2()
            elif self.const == 3:
                load_op = JavaOpcodes.ICONST_3()
            elif self.const == 4:
                load_op = JavaOpcodes.ICONST_4()
            elif self.const == 5:
                load_op = JavaOpcodes.ICONST_5()
            elif self.const == -1:
                load_op = JavaOpcodes.ICONST_M1()
            else:
                load_op = JavaOpcodes.SIPUSH(self.const)
        else:
            prototype = {
                str: '(Ljava/lang/String;)V',
            }[type(self.const)]
            load_op = JavaOpcodes.LDC(self.const)

        context.add_opcodes(
            JavaOpcodes.NEW('org/python/Object'),
            JavaOpcodes.DUP(),
            load_op,
            JavaOpcodes.INVOKESPECIAL('org/python/Object', '<init>', prototype),
        )


class LOAD_NAME(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        context.load_name(self.name)


class BUILD_TUPLE(Opcode):
    def __init__(self, count, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.count = count

    def __arg_repr__(self):
        return str(self.count)

    @property
    def consume_count(self):
        return self.count

    @property
    def product_count(self):
        return 1

    # def convert(self, context, arguments):
    #     code = []
    #     return code


class BUILD_LIST(Opcode):
    def __init__(self, count, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.count = count

    def __arg_repr__(self):
        return str(self.count)

    @property
    def consume_count(self):
        return self.count

    @property
    def product_count(self):
        return 1

    # def convert(self, context, arguments):
    #     code = []
    #     return code


class BUILD_SET(Opcode):
    def __init__(self, count, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.count = count

    def __arg_repr__(self):
        return str(self.count)

    @property
    def consume_count(self):
        return self.count

    @property
    def product_count(self):
        return 1

    # def convert(self, context, arguments):
    #     code = []
    #     return code


class BUILD_MAP(Opcode):
    def __init__(self, count, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.count = count

    def __arg_repr__(self):
        return str(self.count)

    @property
    def consume_count(self):
        return self.count

    @property
    def product_count(self):
        return 1

    # def convert(self, context, arguments):
    #     code = []
    #     return code


class LOAD_ATTR(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        # print("LOAD_ATTR", context, arguments)
        arguments[0].operation.transpile(context, arguments[0].arguments)
        context.add_opcodes(JavaOpcodes.LDC(self.name))

        context.add_opcodes(
            JavaOpcodes.INVOKEVIRTUAL('org/python/Object', '__getattr__', '(Ljava/lang/String;)Lorg/python/Object;'),
        )


class COMPARE_OP(Opcode):
    def __init__(self, comparison, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.comparison = comparison

    def __arg_repr__(self):
        return self.comparison

    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        # Add the operand which will be the left side, and thus the
        # target of the comparator operator.
        arguments[0].operation.transpile(context, arguments[0].arguments)

        # Now add an array of 1 element for the arguments to the comparator
        context.add_opcodes(
            JavaOpcodes.ICONST_1(),
            JavaOpcodes.ANEWARRAY('org/python/Object'),

            JavaOpcodes.DUP(),
            JavaOpcodes.ICONST_0(),
        )
        arguments[1].operation.transpile(context, arguments[1].arguments)
        context.add_opcodes(
            JavaOpcodes.AASTORE(),

            JavaOpcodes.NEW('java/util/Hashtable'),
            JavaOpcodes.DUP(),
            JavaOpcodes.INVOKESPECIAL('java/util/Hashtable', '<init>', '()V'),
        )

        comparator = {
            '<': '__lt__',
            '<=': '__lte__',
            '>': '__gt__',
            '>=': '__gte__',
            '==': '__eq__',
            'exception match': '__eq__',
        }[self.comparison]

        context.add_opcodes(
            JavaOpcodes.INVOKEVIRTUAL('org/python/Object', comparator, '([Lorg/python/Object;Ljava/util/Hashtable;)Lorg/python/Object;')
        )


class IMPORT_NAME(Opcode):
    def __init__(self, target, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.target = target

    def __arg_repr__(self):
        return str(self.target)

    @property
    def consume_count(self):
        return 2

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        pass


class IMPORT_FROM(Opcode):
    def __init__(self, target, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.target = target

    def __arg_repr__(self):
        return str(self.target)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        pass


class JUMP_FORWARD(Opcode):
    def __init__(self, target, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.target = target

    def __arg_repr__(self):
        return str(self.target)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        context.add_opcodes(
            resolve_jump(JavaOpcodes.GOTO(0), context, self.target, Opcode.START)
        )


# class JUMP_IF_FALSE_OR_POP(Opcode):
# class JUMP_IF_TRUE_OR_POP(Opcode):


class JUMP_ABSOLUTE(Opcode):
    def __init__(self, target, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.target = target

    def __arg_repr__(self):
        return str(self.target)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        context.add_opcodes(
            resolve_jump(JavaOpcodes.GOTO(0), context, self.target, Opcode.START)
        )


class POP_JUMP_IF_FALSE(Opcode):
    def __init__(self, target, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.target = target

    def __arg_repr__(self):
        return str(self.target)

    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        context.add_opcodes(
            # (bool) TOS.value
            JavaOpcodes.GETFIELD('org/python/Object', 'value', 'Ljava/lang/Object;'),
            JavaOpcodes.CHECKCAST('java/lang/Boolean'),
            JavaOpcodes.INVOKEVIRTUAL('java/lang/Boolean', 'booleanValue', '()Z'),

            # Jump if false
            resolve_jump(JavaOpcodes.IFEQ(0), context, self.target, Opcode.NEXT)
        )


class POP_JUMP_IF_TRUE(Opcode):
    def __init__(self, target, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.target = target

    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        context.add_opcodes(
            # (bool) TOS.value
            JavaOpcodes.GETFIELD('org/python/Object', 'value', 'java/lang/Object'),
            JavaOpcodes.CHECKCAST('java/lang/Boolean'),
            JavaOpcodes.INVOKEVIRTUAL('java/lang/Boolean', 'booleanValue', '()Z'),

            # Jump if not false
            JavaOpcodes.ICONST_0(),
            resolve_jump(JavaOpcodes.IFNE(0), context, self.target, Opcode.START)
        )


class LOAD_GLOBAL(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        context.load_name(self.name, allow_locals=False)


# class CONTINUE_LOOP(Opcode):


class SETUP_LOOP(Opcode):
    start_block = 'while-loop'

    def __init__(self, delta, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.delta = delta

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        pass


class SETUP_EXCEPT(Opcode):
    start_block = 'except'

    def __init__(self, delta, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.delta = delta

    def __arg_repr__(self):
        return ' %s' % self.delta

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        # print("convert SETUP_FINALLY", len(arguments))
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)


class SETUP_FINALLY(Opcode):
    start_block = 'finally'

    def __init__(self, delta, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.delta = delta

    def __arg_repr__(self):
        return ' %s' % self.delta

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        # print("convert SETUP_FINALLY", len(arguments))
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)


class LOAD_FAST(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        context.add_opcodes(ALOAD_name(context, self.name))


class STORE_FAST(Opcode):
    def __init__(self, name, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.name = name

    def __arg_repr__(self):
        return str(self.name)

    @property
    def consume_count(self):
        return 1

    @property
    def product_count(self):
        return 0

    def convert(self, context, arguments):
        for argument in arguments:
            argument.operation.transpile(context, argument.arguments)

        context.add_opcodes(ASTORE_name(context, self.name))


# class DELETE_FAST(Opcode):


# class RAISE_VARARGS(Opcode):


class CALL_FUNCTION(Opcode):
    def __init__(self, argc, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.args = argc & 0xff
        self.kwargs = ((argc >> 8) & 0xFF)

    def __arg_repr__(self):
        return '%s args, %s kwargs' % (
            self.args,
            self.kwargs,
        )

    @property
    def consume_count(self):
        return 1 + self.args + 2 * self.kwargs

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        code = []

        if arguments[0].operation.opname == 'LOAD_BUILD_CLASS':
            # Construct a class.
            from .klass import Class

            code = arguments[1].arguments[0].operation.const
            class_name = arguments[1].arguments[1].operation.const
            if len(arguments) == 4:
                super_name = arguments[2].operation.const
            else:
                super_name = None

            klass = Class(context.parent, class_name, super_name=super_name)
            klass.extract(code)
            context.parent.classes.append(klass.transpile())
            # print("DESCRIPTOR", klass.descriptor)
            # Push a callable onto the stack so that it can be stored
            # in globals and subsequently retrieved and run.
            context.add_opcodes(
                # Get a Method representing the new function
                TRY(),
                    JavaOpcodes.LDC(Classref(klass.descriptor)),
                    JavaOpcodes.ICONST_2(),
                    JavaOpcodes.ANEWARRAY('java/lang/Class'),
                    JavaOpcodes.DUP(),
                    JavaOpcodes.ICONST_0(),
                    JavaOpcodes.LDC(Classref('[Lorg/python/Object;')),
                    JavaOpcodes.AASTORE(),
                    JavaOpcodes.DUP(),
                    JavaOpcodes.ICONST_1(),
                    JavaOpcodes.LDC(Classref('java/util/Hashtable')),
                    JavaOpcodes.AASTORE(),
                    JavaOpcodes.INVOKEVIRTUAL(
                        'java/lang/Class',
                        'getConstructor',
                        '([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;'
                    ),
                    ASTORE_name(context, '#CONSTRUCTOR#'),

                    # # Then wrap that Constructor into a Callable.
                    JavaOpcodes.NEW('org/python/Constructor'),
                    JavaOpcodes.DUP(),
                    ALOAD_name(context, '#CONSTRUCTOR#'),
                    JavaOpcodes.INVOKESPECIAL('org/python/Constructor', '<init>', '(Ljava/lang/reflect/Constructor;)V'),

                CATCH('java/lang/NoSuchMethodError'),
                    ASTORE_name(context, '#EXCEPTION#'),
                    JavaOpcodes.NEW('org/python/exceptions/RuntimeError'),
                    JavaOpcodes.DUP(),
                    JavaOpcodes.LDC('Unable to find class %s' % (klass.descriptor)),
                    JavaOpcodes.INVOKESPECIAL('org/python/exceptions/RuntimeError', '<init>', '(Ljava/lang/String;)V'),
                    JavaOpcodes.ATHROW(),
                END_TRY()
            )

        else:
            # print("CALL_FUNCTION", context, arguments)

            # Retrive the function
            arguments[0].operation.transpile(context, arguments[0].arguments)

            context.add_opcodes(
                JavaOpcodes.CHECKCAST('org/python/Callable'),
            )

            final_args = self.args
            first_arg = 0

            # If the function has been retrived using LOAD_ATTR, that means
            # it's an instance method. We need to pass the instance itself
            # as the first argument, so make space for that.
            if arguments[0].operation.opname == 'LOAD_ATTR':
                final_args += 1
                first_arg = 1

            context.add_opcodes(
                # Create an array to pass in arguments to invoke()
                ICONST_val(final_args),
                JavaOpcodes.ANEWARRAY('org/python/Object'),
            )

            # If it's an instance method, put the instance at the start of
            # the argument list.
            if arguments[0].operation.opname == 'LOAD_ATTR':
                context.add_opcodes(
                    JavaOpcodes.DUP(),
                    ICONST_val(0),
                )
                arguments[0].arguments[0].operation.transpile(context, arguments[0].arguments[0].arguments)
                context.add_opcodes(JavaOpcodes.AASTORE())

            # Push all the arguments into an array
            for i, argument in enumerate(arguments[1:self.args+1]):
                context.add_opcodes(
                    JavaOpcodes.DUP(),
                    ICONST_val(first_arg + i),
                )
                argument.operation.transpile(context, argument.arguments)
                context.add_opcodes(JavaOpcodes.AASTORE())

            # Create a Hashtable, and push all the kwargs into it.
            context.add_opcodes(
                JavaOpcodes.NEW('java/util/Hashtable'),
                JavaOpcodes.DUP(),
                JavaOpcodes.INVOKESPECIAL('java/util/Hashtable', '<init>', '()V')
            )

            for name, argument in zip(arguments[self.args+1::2], arguments[self.args+2::2]):
                context.add_opcodes(
                    JavaOpcodes.DUP(),
                    JavaOpcodes.LDC(name),
                )
                argument.operation.transpile(context, argument.arguments)
                context.add_opcodes(
                    JavaOpcodes.INVOKEVIRTUAL('java/util/Hashtable', 'put', '(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;'),
                    JavaOpcodes.POP()
                )

            context.add_opcodes(
                JavaOpcodes.INVOKEINTERFACE('org/python/Callable', 'invoke', '([Lorg/python/Object;Ljava/util/Hashtable;)Lorg/python/Object;', 2),
            )
        return code


class MAKE_FUNCTION(Opcode):
    def __init__(self, argc, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.argc = argc
        self.default_args = argc & 0xff
        self.default_kwargs = ((argc >> 8) & 0xFF)
        self.annotations = (argc >> 16) & 0x7FFF

    def __arg_repr__(self):
        return '%d %s default args, %s default kwargs, %s annotations' % (
            self.argc,
            self.default_args,
            self.default_kwargs,
            self.annotations,
        )

    @property
    def consume_count(self):
        if self.annotations:
            return 2 + self.annotations
        else:
            return 2 + self.default_args + (2 * self.default_kwargs)

    @property
    def product_count(self):
        return 1

    def convert(self, context, arguments):
        # Add a new method definition to the context class/module
        code = arguments[-2].operation.const
        full_method_name = arguments[-1].operation.const

        method = context.add_method(full_method_name, code)

        if not method.is_constructor:
            # Push a callable onto the stack so that it can be stored
            # in globals and subsequently retrieved and run.
            context.add_opcodes(
                # Get a Method representing the new function
                TRY(),
                    JavaOpcodes.LDC(Classref(context.descriptor)),
                    JavaOpcodes.LDC(method.name),
                    JavaOpcodes.ICONST_2(),
                    JavaOpcodes.ANEWARRAY('java/lang/Class'),
                    JavaOpcodes.DUP(),
                    JavaOpcodes.ICONST_0(),
                    JavaOpcodes.LDC(Classref('[Lorg/python/Object;')),
                    JavaOpcodes.AASTORE(),
                    JavaOpcodes.DUP(),
                    JavaOpcodes.ICONST_1(),
                    JavaOpcodes.LDC(Classref('java/util/Hashtable')),
                    JavaOpcodes.AASTORE(),
                    JavaOpcodes.INVOKEVIRTUAL(
                        'java/lang/Class',
                        'getMethod',
                        '(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;'
                    ),
                    ASTORE_name(context, '#METHOD#'),

                    # Then wrap that Method into a Callable.
                    JavaOpcodes.NEW(method.callable),
                    JavaOpcodes.DUP(),
                    ALOAD_name(context, '#METHOD#'),
                    JavaOpcodes.INVOKESPECIAL(method.callable, '<init>', '(Ljava/lang/reflect/Method;)V'),
                CATCH('java/lang/NoSuchMethodError'),
                    ASTORE_name(context, '#EXCEPTION#'),
                    JavaOpcodes.NEW('org/python/exceptions/RuntimeError'),
                    JavaOpcodes.DUP(),
                    JavaOpcodes.LDC('Unable to find MAKE_FUNCTION output %s.%s' % (context.module.descriptor, full_method_name)),
                    JavaOpcodes.INVOKESPECIAL('org/python/exceptions/RuntimeError', '<init>', '(Ljava/lang/String;)V'),
                    JavaOpcodes.ATHROW(),
                END_TRY()
            )
        else:
            context.add_opcodes(
                JavaOpcodes.ACONST_NULL()
            )


# class BUILD_SLICE(Opcode):

class MAKE_CLOSURE(Opcode):
    def __init__(self, argc, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.argc = argc

    def __arg_repr__(self):
        return '%s' % (self.argc)

    @property
    def consume_count(self):
        return 3 + self.argc

    @property
    def product_count(self):
        return 1


class LOAD_CLOSURE(Opcode):
    def __init__(self, i, code_offset, starts_line, is_jump_target):
        super().__init__(code_offset, starts_line, is_jump_target)
        self.i = i

    def __arg_repr__(self):
        return '%s' % (self.i)

    @property
    def consume_count(self):
        return 0

    @property
    def product_count(self):
        return 1

    # def convert(self, context, arguments):
    #     return []

# class LOAD_DEREF(Opcode):
# class STORE_DEREF(Opcode):
# class DELETE_DEREF(Opcode):

# class CALL_FUNCTION_KW(Opcode):
# class CALL_FUNCTION_VAR_KW(Opcode):

# class SETUP_WITH(Opcode):
#     start_block = 'finally'
# class LIST_APPEND(Opcode):

# class SET_ADD(Opcode):
# class MAP_ADD(Opcode):

# class LOAD_CLASSDEREF(Opcode):