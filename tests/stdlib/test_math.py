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
