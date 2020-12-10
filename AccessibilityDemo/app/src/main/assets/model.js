const categories = ["Category Zero",
                    "Category One",
                    "Category Two",
                    "Category Three"];

var title = [
    "Movie I",
    "Movie II",
    "Movie III",
    "Movie IV",
    "Movie V",
    "Movie VI",
    "Movie VII",
    "Movie VIII",
    "Movie IX",
    "Movie X",
];

var count = 0;

function getMovieList() {
  var list = [];
  for (var index = 0; index < title.length; ++index) {
    list.push(new Movie(count++, title[index]));
  }
  return list;
}

class Movie {
  constructor(id, title){
    this.id = id;
    this.title = title;
  }
}
