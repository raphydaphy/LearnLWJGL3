#version 400 core

in vec3 position;
in vec2 tex_coords;
in vec3 normal;

out vec2 frag_tex_coords;
out vec3 frag_surface_normal;
out vec3 frag_light_vector;
out vec3 frag_camera_vector;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 view;

uniform vec3 light_position;

void main()
{
    vec4 world_position = transform * vec4(position, 1);

	gl_Position = projection * view * world_position;
	frag_tex_coords = tex_coords;

	frag_surface_normal = (transform * vec4(normal, 0)).xyz;
	frag_light_vector = light_position - world_position.xyz;
	frag_camera_vector = (inverse(view) * vec4(0, 0, 0, 1)).xyz - world_position.xyz;
}
