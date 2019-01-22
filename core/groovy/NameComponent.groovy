package pack.agar.components

import pack.agar.assets.*

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class NameComponent implements Component {
	String name

	NameComponent() {
		name = "Pineapple"
	}
}