#version 120
#extension GL_EXT_gpu_shader4 : require

const vec2 quad_vertices[4] = vec2[4]( vec2( -1.0, -1.0), vec2( 1.0, -1.0), vec2( -1.0, 1.0), vec2( 1.0, 1.0));

void main()
{
    gl_Position = vec4(quad_vertices[gl_VertexID], 0.0, 1.0);
}