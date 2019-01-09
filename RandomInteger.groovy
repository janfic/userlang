package pack.core.scripts

class RandomInteger extends Closure {
	private int min
	private int max

	@Override
	def call() {
		return Math.random() * (max - min) + min
	}

	RandomInteger() {
		super(null)
		min = 0
		max = 10
	}
}
