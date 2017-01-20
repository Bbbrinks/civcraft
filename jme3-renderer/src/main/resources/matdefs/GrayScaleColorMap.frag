varying vec2 texCoord;

uniform vec4 m_Color;
uniform sampler2D m_TextureMap;

void main(){
    vec4 textTexture = texture2D(m_TextureMap, texCoord);
    gl_FragColor = m_Color * textTexture;
}