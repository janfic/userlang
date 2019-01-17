package pack.core.components

import pack.core.assets.*

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class VelocityComponent implements Component {
	int vx
	int vy

	VelocityComponent() {
		vx = 0
		vy = 0
	}
}