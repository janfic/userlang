package pack.core.assets

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class AssetTest extends Closure {
	String name

	@Override
	def call() {
			return new AssetTest()()
	}

	AssetTest() {
		super(null)
		name = "Sprite.png"
	}
}
