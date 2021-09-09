from ..utils import TranspileTestCase

class MathModuleTests(TranspileTestCase):

    def test_sqrt_int(self):
        self.assertCodeExecution("""
            import math
            try:
                print(math.sqrt(4))
                print(math.sqrt(0))
            except Exception as e:
                print(type(e), e)
        """)

    def test_sqrt_float(self):
        self.assertCodeExecution("""
            import math
            try:
                print(math.sqrt(2.1))
                print(math.sqrt(0.0))
            except Exception as e:
                print(type(e), e)
        """)

    def test_sqrt_bool(self):
        self.assertCodeExecution("""
            import math
            try:
                print(math.sqrt(True))
                print(math.sqrt(False))
            except Exception as e:
                print(type(e), e)
        """)

    def test_sqrt_invalid_argument(self):
        self.assertCodeExecution("""
            import math
            try:
                print(math.sqrt(-1))
                print(math.sqrt(-2.1))
                print(math.sqrt("1"))
                print(math.sqrt(None))
            except ValueError as e:
                print(e)
        """)

    def test_fabs_float(self):
        self.assertCodeExecution("""
            import math

            print(math.fabs(-1))
            print(math.fabs(-2))
            print(math.fabs(-100000000))
            print(math.fabs(-1.123456))
            print(math.fabs(100000000))
            print(math.fabs(1.123456))
        """)

    def test_fabs_string(self):
        self.assertCodeExecution("""
            import math

            try:
                print(math.fabs("hi"))
            except Exception as e:
                print(type(e), e)
        """)
                
    def test_ceil_float(self):
        self.assertCodeExecution("""
            import math
            x = 1.78
            print(math.ceil(x))
            x = 0.00000000000000035
            print(math.ceil(x))
            x = 65.453456835682565
            print(math.ceil(x))
            x = -2.78
            print(math.ceil(x))
            x = 0.00000000
            print(math.ceil(x))
        """)

    def test_ceil_int(self):
        self.assertCodeExecution("""
            import math
            x = 34
            print(math.ceil(x))
            x = 3434536345345
            print(math.ceil(x))
            x = -2345
            print(math.ceil(x))
            x = 0
            print(math.ceil(x))
        """)

    def test_ceil_bool(self):
        self.assertCodeExecution("""
            import math
            x = True
            try:
                print(math.ceil(x))
            except TypeError as e:
                print(e)
        """)

    def test_ceil_string_typeerror(self):
        self.assertCodeExecution("""
            import math
            try:
                print(math.ceil("hello"))
            except TypeError as e:
                print(e)
        """)

    def test_ceil_none_typeerror(self):
        self.assertCodeExecution("""
            import math
            try:
                print(math.ceil(None))
            except TypeError as e:
                print(e)
        """)
