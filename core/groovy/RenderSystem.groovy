package pack.agar.systems

import pack.agar.components.*
import pack.agar.assets.*


import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.files.*
import com.badlogic.gdx.math.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.glutil.*

class RenderSystem extends EntitySystem {
	private ImmutableArray<Entity> entities

	private ComponentMapper<PositionComponent> positioncomponentMapper = ComponentMapper.getFor(PositionComponent.class)
	private ComponentMapper<CircleComponent> circlecomponentMapper = ComponentMapper.getFor(CircleComponent.class)

	ShapeRenderer sr

	RenderSystem() {
		sr = new ShapeRenderer()
	}

	void addedToEngine(Engine engine) {
		entities = engine.getFor(
			Family.all(
				PositionComponent.class,
				CircleComponent.class
			).get()
		)
	}

	void update(float deltaTime) {
					sr.begin(ShapeRenderer.ShapeType.Line)
		for( entity in entities) {
			PositionComponent position = positioncomponentMapper.get(entity)
			CircleComponent circle = circlecomponentMapper.get(entity)
						sr.circle(position.x, position.y, circle.radius)
		}
		sr.end()
	}
}
