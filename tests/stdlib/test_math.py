import unittest

from ..utils import TranspileTestCase

class MathModuleTests(TranspileTestCase):
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