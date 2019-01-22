package pack.agar.systems

import pack.agar.components.*
import pack.agar.assets.*


import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.glutil.*

class MassSystem extends EntitySystem {
	private ImmutableArray<Entity> entities

	private ComponentMapper<MassComponent> masscomponentMapper = ComponentMapper.getFor(MassComponent.class)
	private ComponentMapper<CircleComponent> circlecomponentMapper = ComponentMapper.getFor(CircleComponent.class)


	MassSystem() {
	}

	void addedToEngine(Engine engine) {
		entities = engine.getFor(
			Family.all(
				MassComponent.class,
				CircleComponent.class
			).get()
		)
	}

	void update(float deltaTime) {
		for( entity in entities) {
			MassComponent mass = masscomponentMapper.get(entity)
			CircleComponent circle = circlecomponentMapper.get(entity)
						circle.radius = sqrt(mass.mass/Math.PI)
		if(mass.mass <= 0) {
			entity.remove(MassComponent.class)
			engine.add(new PelletEntity()())
		}
		}
	}
}
