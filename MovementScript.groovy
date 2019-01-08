package pack..scripts

class MovementScript extends Closure {
	int x
	int vx
	int y
	int vy

	@Override
	def call() {
		
	x = x + vx
	y = y + vy
	}

	MovementScript() {
		super(null)
	}
}
