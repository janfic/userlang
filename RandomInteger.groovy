package pack.core.scripts

class RandomInteger extends Closure {
	int min
	int max

	@Override
	def call() {
		min + max
	}

	RandomInteger() {
		super(null)
	}
}
