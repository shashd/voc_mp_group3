from .. utils import TranspileTestCase


class MathTests(TranspileTestCase):
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
