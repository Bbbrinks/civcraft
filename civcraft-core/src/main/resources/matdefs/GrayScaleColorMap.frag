varying vec2 texCoord;

uniform sampler2D m_ColorMap;
uniform sampler2D m_TextureMap;

void main(){
    vec4 textTexture = texture2D(m_TextureMap, texCoord);
    float y = 1 -  textTexture.r;
    float x = y * textTexture.r;
    gl_FragColor = texture2D(m_ColorMap, vec2(x, y));
}