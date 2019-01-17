package pack.agar.components

import pack.agar.assets.*

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class MassComponent implements Component {
	int mass

	MassComponent() {
		mass = 12
	}
}