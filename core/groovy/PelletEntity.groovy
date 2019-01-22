package pack.agar.entities

import pack.agar.assets.*
import pack.agar.components.*


import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class PelletEntity extends Closure<Entity> {
	@Override
	Entity call() {
		Entity entity = new Entity()
		entity.add(new MassComponent(mass:4))
		entity.add(new CircleComponent())
		entity.add(new PositionComponent())
		entity.add(new ColorComponent(new RandomColor()()))
		return entity
	}

	PelletEntity() {
		super(null)
	}
}
