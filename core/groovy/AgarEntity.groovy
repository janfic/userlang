package pack.agar.entities

import pack.agar.assets.*
import pack.agar.components.*


import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*

class AgarEntity extends Closure<Entity> {
	@Override
	Entity call() {
		Entity entity = new Entity()
		entity.add(new PositionComponent())
		entity.add(new VelocityComponent())
		entity.add(new MassComponent())
		entity.add(new CircleComponent())
		entity.add(new FollowMouseComponent())
		entity.add(new NameComponent())
		entity.add(new ColorComponent())
		return entity
	}

	AgarEntity() {
		super(null)
	}
}
