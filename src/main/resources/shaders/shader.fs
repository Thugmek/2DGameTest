#version 450 core

out vec4 fragColor;

in vec3 color;
in vec3 uvs;

uniform sampler3D sampler;
uniform int shaderMode;

void main()
{
    if(shaderMode == 1){
        fragColor = texture3D(sampler, uvs);
    }else if(shaderMode == 2){
        fragColor = vec4(0,1,0,1);
    }else if(shaderMode == 3){
        fragColor = texture3D(sampler, uvs);
    }
}