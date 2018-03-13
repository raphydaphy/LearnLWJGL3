#version 400 core

in vec3 frag_surface_normal;
in vec3 frag_light_vector[4];
in vec3 frag_camera_vector;
in float visibility;

flat in vec4 frag_color;

out vec4 out_color;

void main()
{
   out_color = frag_color;
}
