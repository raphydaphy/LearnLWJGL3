#version 330

in vec2 frag_textures;

out vec4 out_colour;

uniform sampler2D modelTexture;

void main(void)
{
    float alpha = texture(modelTexture, frag_textures).a;
    if (alpha < 0.5)
    {
    }
	out_colour = vec4(1.0);
}