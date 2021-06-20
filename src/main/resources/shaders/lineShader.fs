#version 450 core

out vec4 fragColor;


uniform sampler2D ourTexture;

void main()
{
    fragColor = vec4(1,1,1,1.0);
}