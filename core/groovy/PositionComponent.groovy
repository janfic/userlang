package pack.core.components

import pack.core.assets.*

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class PositionComponent implements Component {
	int x
	int y

	PositionComponent() {
		x = 0
		y = 0
	}
}