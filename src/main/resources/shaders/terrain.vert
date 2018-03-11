#version 400 core

in vec3 position;
in vec2 tex_coords;
in vec3 normal;

out vec2 frag_tex_coords;
flat out vec3 frag_surface_normal;
out vec3 frag_light_vector[4];
out vec3 frag_camera_vector;
out float visibility;

flat out vec4 frag_color;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 view;

uniform vec3 light_position[4];

const float fog_density = 0.005;
const float fog_gradient = 4;

void main()
{
    vec4 world_position = transform * vec4(position, 1);

    vec4 relative_position = view * world_position;
	gl_Position = projection * relative_position;
	frag_tex_coords = tex_coords;

	frag_surface_normal = (transform * vec4(normal, 0)).xyz;

	for (int i = 0; i < 4; i++)
	{
	    frag_light_vector[i] = light_position[i] - world_position.xyz;
	}
	frag_camera_vector = (inverse(view) * vec4(0, 0, 0, 1)).xyz - world_position.xyz;

	float camera_distance = length(relative_position.xyz);
    visibility = exp(-pow((camera_distance*fog_density), fog_gradient));
    visibility = clamp(visibility, 0, 1);

    if (world_position.y < 5)
    {
        frag_color = vec4(0,0,1,1);
    }
    else if (world_position.y < 10)
    {
        frag_color = vec4(1,1,0,1);
    }
    else if (world_position.y < 15)
    {
        frag_color = vec4(0,1,0,1);
    }
    else if (world_position.y < 25)
    {
        frag_color = vec4(0,1,1,1);
    }
    else
    {
        frag_color = vec4(1,1,1,1);
    }
}
