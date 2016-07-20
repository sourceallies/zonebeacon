if [[ ${LINK_TO_KEYSTORE} == *"s3.amazonaws.com"* ]]; then
	# if the key is stored in amazon s3, then we will assume the bucket is private.
	contentType="application/octet-stream" 
	dateValue="`date +'%a, %d %b %Y %H:%M:%S %z'`" 
	stringToSign="GET\n\n${contentType}\n${dateValue}\n${resource}"
	signature=`/bin/echo -en "$stringToSign" | openssl sha1 -hmac ${S3_SECRET} -binary | base64` 

	curl -H "Date: ${dateValue}" -H "Content-Type: ${contentType}" -H "Authorization: AWS ${S3_KEY}:${signature}" ${LINK_TO_KEYSTORE} -o "keystore.jks"
else
	# implementation for a publicly downloadable keystore (not recommended)
	curl -o keystore.jks ${LINK_TO_KEYSTORE}
fi


cat > keystore.properties << EOF
keystorepassword=${KEY_STORE_PASSWORD}
keyalias=${KEY_ALIAS}
keypassword=${KEY_PASSWORD}
EOF

cat > api_keys.properties << EOF
NEARBY=${NEARBY}
EOF