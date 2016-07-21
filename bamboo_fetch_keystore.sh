#!/bin/bash
# script used by bamboo to deploy to AWS

set -e

#look up the ARN for the environment we are deploying into
adminARN="$(printenv bamboo_SAI_${DEPLOYMENT_ENVIRONMENT}_ADMIN_ARN )"
echo "Assuming role: $adminARN"
source /bin/assumeRole $adminARN

aws --region us-east-1 s3 cp s3://${KEYSTORE_S3_BUCKET}/zonebeacon/keystore.jks keystore.jks

cat > keystore.properties << EOF
keystorepassword=${KEY_STORE_PASSWORD}
keyalias=${KEY_ALIAS}
keypassword=${KEY_PASSWORD}
EOF

cat > api_keys.properties << EOF
NEARBY=${NEARBY}
EOF