package pack.core.assets

class AssetTest extends Closure {
	class java.lang.String name

	@Override
	def call() {
		return new Sprite(name)
	}

	AssetTest() {
		super(null)
		name = Sprite.png
	}
}
