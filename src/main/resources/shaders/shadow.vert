#version 150

in vec3 in_position;
in vec2 in_textures;

out vec2 frag_textures;

uniform mat4 mvpMatrix;

void main(void)
{
	gl_Position = mvpMatrix * vec4(in_position, 1.0);
	frag_textures = in_textures;
}