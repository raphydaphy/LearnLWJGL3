#version 400 core

in vec3 frag_surface_normal;
in vec3 frag_light_vector[4];
in vec3 frag_camera_vector;

in vec4 shadow_coords;

flat in vec4 frag_color;
flat in vec3 total_diffuse;
flat in vec3 total_specular;
flat in float visibility;

out vec4 out_color;

uniform vec3 sky_color;
uniform sampler2D shadow_map;

void main()
{
    float object_nearest_light = texture(shadow_map, shadow_coords.xy).r;
    float light_factor = 1;
    if (shadow_coords.z > object_nearest_light)
    {
        light_factor = 1 - 0.4;
    }

    vec3 shadow_diffuse = max(total_diffuse, 0.4) * light_factor;

    out_color = vec4(total_diffuse, 1) * frag_color + vec4(total_specular, 1);
    out_color = mix(vec4(sky_color, 1), frag_color, visibility);
}
