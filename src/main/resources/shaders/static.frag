#version 400 core

in vec2 frag_tex_coords;

out vec4 out_color;

uniform sampler2D sampler;

void main()
{
    out_color = texture(sampler, frag_tex_coords);
}
