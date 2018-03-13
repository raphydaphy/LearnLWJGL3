#version 400 core

in vec3 position;
in vec3 normal;

out vec3 frag_surface_normal;
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

uniform sampler2D sampler;
uniform vec3 light_color[4];
uniform vec3 light_attenuation[4];
uniform float shine_damper;
uniform float reflectivity;
uniform vec3 sky_color;

void main()
{
    vec4 world_position = transform * vec4(position, 1);

    vec4 relative_position = view * world_position;
	gl_Position = projection * relative_position;

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

     vec3 unit_normal = normalize(frag_surface_normal);
    vec3 unit_camera_vector = normalize(frag_camera_vector);

    vec3 total_diffuse = vec3(0);
    vec3 total_specular = vec3(0);

    for (int i = 0; i < 4; i++)
    {
        float distance = length(frag_light_vector[i]);
        float attFactor = light_attenuation[i].x + (light_attenuation[i].y * distance) + (light_attenuation[i].z * distance * distance);
        vec3 unit_light_vector = normalize(frag_light_vector[i]);

        float light_angle = dot(unit_normal, unit_light_vector);
        float brightness = max(light_angle, 0);

        vec3 light_direction = -unit_light_vector;
        vec3 reflection = reflect(light_direction, unit_normal);

        float reflection_angle = dot(reflection, unit_camera_vector);
        reflection_angle = max(reflection_angle, 0);

        float dampening_factor = pow(reflection_angle, shine_damper);

        total_diffuse = total_diffuse + (brightness * light_color[i]) / attFactor;
        total_specular = total_specular + (dampening_factor * reflectivity * light_color[i]) / attFactor;
    }

    frag_color = vec4(total_diffuse, 1) * frag_color + vec4(total_specular, 1);
    frag_color = mix(vec4(sky_color, 1), frag_color, visibility);
}
