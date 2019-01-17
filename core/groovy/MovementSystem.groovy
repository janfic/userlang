package pack.agar.systems

import pack.agar.components.*
import pack.agar.assets.*

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.glutil.*

class MovementSystem extends EntitySytem {
	private ImmutableArray<Entity> entities

	private ComponentMapper<PositionComponent> positioncomponentMapper = ComponentMapper.getFor(PositionComponent.class)
	private ComponentMapper<VelocityComponent> velocitycomponentMapper = ComponentMapper.getFor(VelocityComponent.class)


	MovementSystem() {
	}

	void addedToEngine(Engine engine) {
		entities = engine.getFor(
			Family.all(
				PositionComponent.class,
				VelocityComponent.class
			).get()
		)
	}

	void update(float deltaTime) {
		for( entity in entities) {
			PositionComponent position = positioncomponentMapper.get(entity)
			VelocityComponent velocity = velocitycomponentMapper.get(entity)
			position.x += velocity.vx
		position.y += velocity.vy
		}
	}
}
