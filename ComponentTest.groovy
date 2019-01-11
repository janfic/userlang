package pack.core.components

import pack.core.assets.*

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class ComponentTest implements Component {
	int x
	Sprite y
	String z

	ComponentTest() {
		x = 0
		y = new PlayerSprite()()
		z = "String"
	}
}