package pack.agar.systems

import pack.agar.components.*
import pack.agar.assets.*

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.glutil.*

class FollowMouseSystem extends EntitySytem {
	private ImmutableArray<Entity> entities

	private ComponentMapper<PositionComponent> positioncomponentMapper = ComponentMapper.getFor(PositionComponent.class)
	private ComponentMapper<VelocityComponent> velocitycomponentMapper = ComponentMapper.getFor(VelocityComponent.class)
	private ComponentMapper<FollowMouseComponent> followmousecomponentMapper = ComponentMapper.getFor(FollowMouseComponent.class)


	FollowMouseSystem() {
	}

	void addedToEngine(Engine engine) {
		entities = engine.getFor(
			Family.all(
				PositionComponent.class,
				VelocityComponent.class,
				FollowMouseComponent.class
			).get()
		)
	}

	void update(float deltaTime) {
		float mx = Gdx.input.getX()
		float my = Gdx.input.getY()
		velocity.vx = (mx - position.x) / (my - position.y + mx - position.x)
		velocity.vx = (my - position.y) / (my - position.y + mx - position.x)
	}
}
