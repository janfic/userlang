package pack.core.scripts

class RandomInteger extends Closure {
	int min
	int max

	@Override
	def call() {
		return Math.random() * (max - min) + min
	}

	RandomInteger() {
		super(null)
	}
}
