malletPath="/home/ykartal/apps/mallet-2.0.8RC3/bin/mallet"
$malletPath train-topics --input $1.mallet --num-topics $2 --output-state $1-state.gz --output-topic-keys $1_keys.txt --topic-word-weights-file $1-wordweights.txt --word-topic-counts-file $1-wordtopiccount.txt --output-doc-topics $1_composition.txt --num-threads 1 $3
