package pack.core.components

import pack.core.scripts.*
import pack.core.assets.*

import com.badlogic.ashley.core.Component

class TestComponent implements Component {
	int x
	int y
	int z

	TestComponent() {
		x = 0
		y = 0
		z = new MovementScript(x:0, vx:12, y:0, vy:13)()
	}
}