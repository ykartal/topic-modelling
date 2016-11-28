malletPath="/home/ykartal/apps/mallet-2.0.8RC3/bin/mallet"
$malletPath import-dir --input ./$1/ --output $1.mallet --keep-sequence --remove-stopwords
$malletPath split --input $1.mallet --training-file $1-train.mallet --testing-file $1-test.mallet --training-portion 0.8
