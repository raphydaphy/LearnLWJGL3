#version 400 core

in vec3 position;
in vec2 tex_coords;

out vec2 frag_tex_coords;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 view;

void main()
{
	gl_Position = projection * view * transform * vec4(position, 1);
	frag_tex_coords = tex_coords;
}
