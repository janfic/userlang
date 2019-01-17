package pack.agar.systems

import pack.agar.components.*
import pack.agar.assets.*

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.glutil.*

class CollisionSystem extends EntitySytem {
	private ImmutableArray<Entity> entities

	private ComponentMapper<MassComponent> masscomponentMapper = ComponentMapper.getFor(MassComponent.class)
	private ComponentMapper<PositionComponent> positioncomponentMapper = ComponentMapper.getFor(PositionComponent.class)
	private ComponentMapper<CircleComponent> circlecomponentMapper = ComponentMapper.getFor(CircleComponent.class)


	CollisionSystem() {
	}

	void addedToEngine(Engine engine) {
		entities = engine.getFor(
			Family.all(
				MassComponent.class,
				PositionComponent.class,
				CircleComponent.class
			).get()
		)
	}

	void update(float deltaTime) {
	}
}
