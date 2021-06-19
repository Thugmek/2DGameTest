#version 450 core

out vec4 fragColor;

in vec3 color;
in vec2 uvs;

uniform sampler2D ourTexture;

void main()
{
    //fragColor = vec4(color[0]*lf*darken, color[1]*lf*darken, color[2]*lf*darken + (1-darken)*0.5, 1.0);
    //fragColor = vec4(color.x,color.y,color.z,1.0);
    fragColor = texture(ourTexture, uvs);
}