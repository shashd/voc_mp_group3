from ..utils import TranspileTestCase

class TestTimeDelta(TranspileTestCase):

    def test_sqrt_int(self):
        self.assertCodeExecution("""
            from datetime import timedelta

            print(timedelta(1).days)
            print(timedelta(seconds = 2).seconds)
            print(timedelta(days = 3).days)

            try:
                print(timedelta(1, days=2))
            except Exception as e:
                print(type(e), e)
        """)
