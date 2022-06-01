#version 330

in vec2 outTexCoord;
in vec3 mvNormalVertex;
in vec3 mvPosVertex;

out vec4 fragColor;


const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
// Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

struct DirectionalLight
{
    vec3 color;
    vec3 direction;
    float intensity;

};

struct SpotLight
{
    PointLight pointLight;
    vec3 coneDirection;
    float range;

};
uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;



vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

void setupColors(Material material, vec2 textCoord)
{
    if (material.hasTexture == 1)
    {
        ambientC = texture(texture_sampler, textCoord);
        diffuseC = ambientC;
        speculrC = ambientC;
    }
    else
    {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        speculrC = material.specular;
    }
}

vec4 calcLightColor(vec3 light_color, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColor = diffuseC * vec4(light_color, 1.0) * light_intensity *
    diffuseFactor;

    vec3 camera_direction = normalize(- position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir, normal));
    float specularFactor = max(dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColor = speculrC * light_intensity*specularFactor * material
    .reflectance * vec4(light_color, 1.0);

    return (diffuseColor + specColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    // Diffuse Light
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_color = calcLightColor(light.color, light.intensity, position,
    to_light_dir, normal);


    // Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
    light.att.exponent * distance * distance;
    return light_color/ attenuationInv;
}

vec4 calcSpotLight(SpotLight spotLight, vec3 position, vec3 normal)
{
    vec3 light_direction = spotLight.pointLight.position - position;
    vec3 to_light_dir = normalize(light_direction);

    vec3 from_light_dir = -to_light_dir;
    float spotAngle = dot(from_light_dir, normalize(spotLight.coneDirection));

    vec4 color = vec4(0, 0, 0, 0);

    if (spotAngle>spotLight.range){
        color = calcPointLight(spotLight.pointLight, position, normal);
        color  *= (1-(1-spotAngle)/(1-spotLight.range));
    }
    return color;
}



vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main()
{
    setupColors(material, outTexCoord);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight,
    mvPosVertex,
    mvNormalVertex);

    for(int i = 0; i < MAX_POINT_LIGHTS; i++){
        if(pointLights[i].intensity>0){
            diffuseSpecularComp += calcPointLight(pointLights[i],
            mvPosVertex, mvNormalVertex);
        }
    }

    for(int i = 0; i < MAX_SPOT_LIGHTS; i++){
        if(spotLights[i].pointLight.intensity>0){
            diffuseSpecularComp += calcSpotLight(spotLights[i],
            mvPosVertex, mvNormalVertex);
        }
    }


    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}