curl -o keystore.jks ${LINK_TO_KEYSTORE}

cat > keystore.properties << EOF
keystorepassword=${KEY_STORE_PASSWORD}
keyalias=${KEY_ALIAS}
keypassword=${KEY_PASSWORD}
EOF

cat > api_keys.properties << EOF
NEARBY=${NEARBY}
EOF