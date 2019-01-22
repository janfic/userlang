package pack.core.assets

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class RandomInteger extends Closure {
	int min
	int max
	Random rand

	@Override
	def call() {
			
		return rand.nextInt(max-min) + min

	}

	RandomInteger() {
		super(null)
		min = 0
		max = 10
		rand = new Random()
	}
}
