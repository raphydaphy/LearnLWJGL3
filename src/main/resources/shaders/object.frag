#version 400 core

in vec2 frag_tex_coords;
in vec3 frag_surface_normal;
in vec3 frag_light_vector;
in vec3 frag_camera_vector;
in float visibility;

out vec4 out_color;

uniform sampler2D sampler;
uniform vec3 light_color;
uniform float shine_damper;
uniform float reflectivity;
uniform vec3 sky_color;

void main()
{
    vec3 unit_normal = normalize(frag_surface_normal);
    vec3 unit_light_vector = normalize(frag_light_vector);

    float light_angle = dot(unit_normal, unit_light_vector);
    float brightness = max(light_angle, 0.2);

    vec3 diffuse = brightness * light_color;

    vec3 unit_camera_vector = normalize(frag_camera_vector);
    vec3 light_direction = -unit_light_vector;
    vec3 reflection = reflect(light_direction, unit_normal);

    float reflection_angle = dot(reflection, unit_camera_vector);
    reflection_angle = max(reflection_angle, 0);

    float dampening_factor = pow(reflection_angle, shine_damper);
    vec3 specular = dampening_factor * reflectivity * light_color;

    vec4 texture_color = texture(sampler, frag_tex_coords);

    if (texture_color.a < 0.5)
    {
        discard;
    }

    out_color = vec4(diffuse, 1) * texture_color + vec4(specular, 1);
    out_color = mix(vec4(sky_color, 1), out_color, visibility);
}
