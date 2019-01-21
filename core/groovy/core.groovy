package pack.core

class Core extends Pack {
	Core() {
		super(
			name:"Core",
			display:"CORE",
			info:"The core engine of the game",
			dependencies:[],
			assets:[],
			components:["PositionComponent","VelocityComponent","FollowMouseComponent"],
			entities:[],
			systems:["MovementSystem","FollowMouseSystem","RenderSystem"]
		)
	}
}
