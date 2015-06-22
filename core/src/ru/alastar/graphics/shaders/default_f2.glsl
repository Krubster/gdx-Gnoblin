#ifdef GL_ES 
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

#if defined(specularTextureFlag) || defined(specularColorFlag)
#define specularFlag
#endif

#ifdef normalFlag
varying vec3 v_normal;
#endif //normalFlag

#if defined(colorFlag)
varying vec4 v_color;
#endif

#ifdef blendedFlag
varying float v_opacity;
#ifdef alphaTestFlag
varying float v_alphaTest;
#endif //alphaTestFlag
#endif //blendedFlag

#if defined(diffuseTextureFlag) || defined(specularTextureFlag)
#define textureFlag
#endif

#ifdef diffuseTextureFlag
varying MED vec2 v_diffuseUV;
#endif

#ifdef specularTextureFlag
varying MED vec2 v_specularUV;
#endif

#ifdef diffuseColorFlag
uniform vec4 u_diffuseColor;
#endif

#ifdef diffuseTextureFlag
uniform sampler2D u_diffuseTexture;
#endif

#ifdef specularColorFlag
uniform vec4 u_specularColor;
#endif

#ifdef specularTextureFlag
uniform sampler2D u_specularTexture;
#endif

#ifdef normalTextureFlag
uniform sampler2D u_normalTexture;
#endif

#ifdef lightingFlag
//varying vec3 lightDiffuse;

#if	defined(ambientLightFlag) || defined(ambientCubemapFlag) || defined(sphericalHarmonicsFlag)
#define ambientFlag
#endif //ambientFlag

#ifdef specularFlag
varying vec3 v_lightSpecular;
#endif //specularFlag
varying vec4 v_position;

#ifdef shadowMapFlag

#if defined(numLights) && (numLights > 0)
struct Light
{
	vec3 color;
	vec3 direction;
	vec3 position;

	mat4 projView;

	sampler2D depthMap;

	float offset;
	float intensity;
	float angle;

	int type;

};
uniform Light u_Lights[numLights];
#endif // numLights

#endif //shadowMapFlag

#if defined(ambientFlag) && defined(separateAmbientFlag)
varying vec3 v_ambientLight;
#endif //separateAmbientFlag

#endif //lightingFlag

#ifdef fogFlag
uniform vec4 u_fogColor;
varying float v_fog;
#endif // fogFlag

void main() {
const mat4 biasMat = mat4(
                0.5f, 0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.0f, 0.5f,
                0.0f, 0.0f, 0.5f, 0.5f,
                0.0f, 0.0f, 0.0f, 1.0f
                          );
 float visibility = 1.0;
 float bias = 0.005;
 vec3 lightDiffuse;
 for(int i = 0; i < numLights; ++i)
 {
  if(u_Lights[i].type == 1){
 				vec3 lightDir = -u_Lights[i].direction;
 				float NdotL = clamp(dot(v_normal, lightDir), 0.0, 1.0);
 				vec3 value = u_Lights[i].color * NdotL;
 				lightDiffuse += value;
 				#ifdef specularFlag
 					float halfDotView = max(0.0, dot(v_normal, normalize(lightDir + viewVec)));
 					v_lightSpecular += value * pow(halfDotView, u_shininess);
 				#endif // specularFlag
 			}
 			else if(u_Lights[i].type == 2)
 			{
 			    float attenuation = 1.0f;
 			    vec3 surfaceToLight = normalize(u_Lights[i].position - v_position).xyz;
                float lightToSurfaceAngle = degrees(acos(dot(-surfaceToLight, normalize(u_Lights[i].direction))));
                if(lightToSurfaceAngle >= u_Lights[i].angle){
                     attenuation = 0.0;
                }
                float diffuseCoefficient = max(0.0, dot(v_normal, v_position.xyz));
                lightDiffuse +=  u_Lights[i].color * diffuseCoefficient * attenuation;
 			}
 			else if(u_Lights[i].type == 3)
 			{
 			    vec3 lightDir = u_Lights[i].position - v_position.xyz;
                 				float dist2 = dot(lightDir, lightDir);
                 				lightDir *= inversesqrt(dist2);
                 				float NdotL = clamp(dot(v_normal, lightDir), 0.0, 1.0);
                 				vec3 value = u_Lights[i].color * (NdotL / (1.0 + dist2));
                 				lightDiffuse += value;
                 				#ifdef specularFlag
                 					float halfDotView = max(0.0, dot(v_normal, normalize(lightDir + viewVec)));
                 					v_lightSpecular += value * pow(halfDotView, u_shininess);
                 				#endif // specularFlag
 			}
    vec4 ShadowCoord = u_Lights[i].projView * v_position * biasMat;


    if ( texture2D( u_Lights[i].depthMap, ShadowCoord.xy ).z  <  ShadowCoord.z - bias)
    {
     if(u_Lights[i].type == 1)
        visibility = 0.5;
    }
 }
		#ifdef specularFlag
			v_lightSpecular = vec3(0.0);
			vec3 viewVec = normalize(u_cameraPosition.xyz - pos.xyz);
		#endif // specularFlag

 	#if defined(normalFlag)
 		vec3 normal = v_normal;
 	#endif // normalFlag

 	#if defined(diffuseTextureFlag) && defined(diffuseColorFlag) && defined(colorFlag)
 		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor * v_color;
 	#elif defined(diffuseTextureFlag) && defined(diffuseColorFlag)
 		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor;
 	#elif defined(diffuseTextureFlag) && defined(colorFlag)
 		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * v_color;
 	#elif defined(diffuseTextureFlag)
 		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV);
 	#elif defined(diffuseColorFlag) && defined(colorFlag)
 		vec4 diffuse = u_diffuseColor * v_color;
 	#elif defined(diffuseColorFlag)
 		vec4 diffuse = u_diffuseColor;
 	#elif defined(colorFlag)
 		vec4 diffuse = v_color;
 	#else
 		vec4 diffuse = vec4(1.0);
 	#endif

 	#if (!defined(lightingFlag))
 		gl_FragColor.rgb = diffuse.rgb;
 	#elif (!defined(specularFlag))
 		#if defined(ambientFlag) && defined(separateAmbientFlag)
 			#ifdef shadowMapFlag
 				gl_FragColor.rgb = (diffuse.rgb * (v_ambientLight + visibility * lightDiffuse));
 			#else
 				gl_FragColor.rgb = (diffuse.rgb * (v_ambientLight + lightDiffuse));
 			#endif //shadowMapFlag
 		#else
 			#ifdef shadowMapFlag
  			    gl_FragColor.rgb = diffuse.rgb * lightDiffuse * visibility;
 			#else
 				gl_FragColor.rgb = (diffuse.rgb * lightDiffuse);
 			#endif //shadowMapFlag
 		#endif
 	#else
 		#if defined(specularTextureFlag) && defined(specularColorFlag)
 			vec3 specular = texture2D(u_specularTexture, v_specularUV).rgb * u_specularColor.rgb * v_lightSpecular;
 		#elif defined(specularTextureFlag)
 			vec3 specular = texture2D(u_specularTexture, v_specularUV).rgb * v_lightSpecular;
 		#elif defined(specularColorFlag)
 			vec3 specular = u_specularColor.rgb * v_lightSpecular;
 		#else
 			vec3 specular = v_lightSpecular;
 		#endif

 		#if defined(ambientFlag) && defined(separateAmbientFlag)
 			#ifdef shadowMapFlag
 			gl_FragColor.rgb = (diffuse.rgb * (visibility * lightDiffuse + v_ambientLight)) + specular;
 			#else
 				gl_FragColor.rgb = (diffuse.rgb * (lightDiffuse + v_ambientLight)) + specular;
 			#endif //shadowMapFlag
 		#else
 			#ifdef shadowMapFlag
            	gl_FragColor.rgb = ((diffuse.rgb * lightDiffuse) + specular) * visibility;
 			#else
 				gl_FragColor.rgb = ((diffuse.rgb * lightDiffuse) + specular);
 			#endif //shadowMapFlag
 		#endif
 	#endif //lightingFlag

 	#ifdef fogFlag
 		gl_FragColor.rgb = mix(gl_FragColor.rgb, u_fogColor.rgb, v_fog);
 	#endif // end fogFlag

 	#ifdef blendedFlag
 		gl_FragColor.a = diffuse.a * v_opacity;
 		#ifdef alphaTestFlag
 			if (gl_FragColor.a <= v_alphaTest)
 				discard;
 		#endif
 	#else
 		gl_FragColor.a = 1.0;
 	#endif
}
