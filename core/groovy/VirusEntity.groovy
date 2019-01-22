package pack.agar.entities

import pack.agar.assets.*
import pack.agar.components.*


import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class VirusEntity extends Closure<Entity> {
	@Override
	Entity call() {
		Entity entity = new Entity()
		entity.add(new PositionComponent())
		entity.add(new CircleComponent())
		entity.add(new MassComponent(mass:100))
		entity.add(new PopComponent())
		return entity
	}

	VirusEntity() {
		super(null)
	}
}
