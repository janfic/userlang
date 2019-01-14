package pack.core.systems

import pack.core.assets.*
import pack.core.components.*

import com.badlogic.ashley.core.*

class SystemTest extends EntitySystem {
	private ImmutableArray<Entity> entities
	
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class)
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class)

	void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(
			Family.all(
				PositionComponent.class, 
				VelocityComponent.class
			).get()
		)
	}
	
	void update(float deltaTime) {
		for(entity in entities) {
			PositionComponent pc = pm.get(entity)
			VelocityComponent vc = vm.get(entity)
			
			pc.x += vc.vx
			pc.y += vc.vy
		}
	}
}