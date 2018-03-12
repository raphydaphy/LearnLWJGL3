#version 400 core

in vec2 frag_uvs;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D font;

void main(void)
{
    out_color = vec4(color, texture(font, frag_uvs).a);
}