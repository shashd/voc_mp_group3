import math
from .. utils import TranspileTestCase, BuiltinFunctionTestCase

class MathTests(TranspileTestCase):
	def test_float_ceil(self):
		self.assertCodeExecution("""
			import math
			x = 1.78
			print(math.ceil(x))
		""")
	def test_neg_float_ceil(self):
		self.assertCodeExecution("""
			import math
			x = -2.78
			print(math.ceil(x))
		""")
	def test_int_ceil(self):
		self.assertCodeExecution("""
			import math
			x = 34
			print(math.ceil(x))
		""")
	def test_neg_int_ceil(self):
		self.assertCodeExecution("""
			import math
			x = -2
			print(math.ceil(x))
		""")