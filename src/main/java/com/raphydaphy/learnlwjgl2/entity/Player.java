package main.java.com.raphydaphy.learnlwjgl2.entity;

import main.java.com.raphydaphy.learnlwjgl2.models.TexturedModel;
import main.java.com.raphydaphy.learnlwjgl2.render.ModelTransform;
import main.java.com.raphydaphy.learnlwjgl2.render.Transform;
import main.java.com.raphydaphy.learnlwjgl2.renderengine.DisplayManager;
import main.java.com.raphydaphy.learnlwjgl2.terrain.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Player
{
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean jumping = false;

	public ModelTransform data;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale)
	{
		this.data = new ModelTransform(new Transform(position, rotX, rotY, rotZ, scale), model);
	}

	public void move(Terrain terrain)
	{
		float delta = DisplayManager.getFrameTimeSeconds();

		doInput();
		data.getTransform().rotate(0, currentTurnSpeed * delta, 0);


		float distance = currentSpeed * delta;

		float xMove = (float) Math.sin(Math.toRadians(data.getTransform().getRotY())) * distance;
		float zMove = (float) Math.cos(Math.toRadians(data.getTransform().getRotY())) * distance;

		upwardsSpeed += GRAVITY * delta;

		data.getTransform().move(xMove, upwardsSpeed * delta, zMove);

		float ground = terrain.getHeight(data.getTransform().getPosition().x, data.getTransform().getPosition().z);
		if (data.getTransform().getPosition().y < ground)
		{
			jumping = false;
			upwardsSpeed = 0;
			data.getTransform().getPosition().y = ground;
		}

	}

	private void doInput()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			currentSpeed = -RUN_SPEED;
		} else
		{
			currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			currentTurnSpeed = TURN_SPEED;
		} else
		{
			currentTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			jump();
		}
	}

	private void jump()
	{
		if (!jumping)
		{
			this.upwardsSpeed = JUMP_POWER;
			jumping = true;
		}
	}
}
