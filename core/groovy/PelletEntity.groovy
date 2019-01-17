package pack.agar.entities

import pack.agar.assets.*
import pack.agar.components.*

import com.badlogic.ashley.core.Entity

class PelletEntity extends Closure<Entity> {
	@Override
	Entity call() {
		Entity entity = new Entity()
		entity.add(new MassComponent(mass:4))
		entity.add(new CircleComponent())
		entity.add(new PositionComponent())
		return entity
	}

	PelletEntity() {
		super(null)
	}
}
